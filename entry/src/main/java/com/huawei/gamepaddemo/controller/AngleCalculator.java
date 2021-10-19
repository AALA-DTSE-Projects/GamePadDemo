/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.gamepaddemo.controller;

import ohos.agp.components.Component;
import ohos.multimodalinput.event.TouchEvent;

public class AngleCalculator {
    private static final String TAG = "AngleUtil";

    private int angle;

    private float startPosX;

    private float startPosY;

    private float moveX;

    private float moveY;

    private final Component layout;

    private final int screenHeight;

    private final Component smallCircle;

    private final Component bigCircle;

    private int smallR;

    private int bigR;

    private final Listener listener;

    public interface Listener {
        void sendAngle(int angle);
    }

    public AngleCalculator(Component smallCircle, Component bigCircle, Component layout, int screenHeight, Listener listener) {
        this.smallCircle = smallCircle;
        this.bigCircle = bigCircle;
        this.layout = layout;
        this.screenHeight = screenHeight;
        this.listener = listener;
    }

    public Component.TouchEventListener getOnTouchEvent() {
        return (component, touchEvent) -> {
            int layoutHeight = layout.getHeight();
            int height = screenHeight - layoutHeight;
            smallR = smallCircle.getWidth() / Const.QUADRANT_2;
            bigR = bigCircle.getWidth() / Const.QUADRANT_2;
            int action = touchEvent.getAction();
            switch (action) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    startPosX = bigR + bigCircle.getContentPositionX();
                    startPosY = bigR + bigCircle.getContentPositionY();
                    LogUtil.debug(TAG, "point down (" + startPosX + ";" + startPosY + ")");
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                    smallCircle.setContentPosition(startPosX - smallR, startPosY - smallR);
                    angle = 0;
                    LogUtil.debug(TAG, "point up");
                    break;
                case TouchEvent.POINT_MOVE:
                    moveX = touchEvent.getPointerPosition(0).getX();
                    moveY = touchEvent.getPointerScreenPosition(0).getY() - height;
                    float smallCurrX = moveX - smallR;
                    float smallCurrY = moveY - smallR;
                    smallCircle.setVisibility(Component.VISIBLE);
                    float[] smallCurrentPosition = getSmallCurrentPos(smallCurrX, smallCurrY);
                    smallCircle.setContentPosition(
                            smallCurrentPosition[0],
                            smallCurrentPosition[1]
                    );
                    calculateAngle();
                    LogUtil.debug(TAG, "point move with angle " + angle);
                    if (listener != null) {
                        listener.sendAngle(angle);
                    }
                    break;
                default:
                    return false;
            }
            return true;
        };
    }

    private void calculateAngle() {
        int degree = (int) Math.toDegrees(Math.atan(getDisAbsY() / getDisAbsX()));
        int quadrant = quadrant();
        switch (quadrant) {
            case Const.QUADRANT_1:
                angle = degree;
                break;
            case Const.QUADRANT_2:
                angle = Const.DEGREE_180 - degree;
                break;
            case Const.QUADRANT_3:
                angle = -Const.DEGREE_180 + degree;
                break;
            case Const.QUADRANT_4:
                angle = -degree;
                break;
            default:
                angle = 0;
                break;
        }
    }

    private int quadrant() {
        if (getFlagX() && !getFlagY()) {
            return Const.QUADRANT_1;
        } else if (!getFlagX() && !getFlagY()) {
            return Const.QUADRANT_2;
        } else if (!getFlagX() && getFlagY()) {
            return Const.QUADRANT_3;
        } else if (getFlagX() && getFlagY()) {
            return Const.QUADRANT_4;
        } else {
            return 0;
        }
    }

    private boolean getFlagX() {
        return moveX - startPosX > 0;
    }

    private boolean getFlagY() {
        return moveY - startPosY > 0;
    }

    private float getDisAbsX() {
        float disAbsX = Math.abs(moveX - startPosX);
        if (disAbsX < Const.MIN_SLIDE) {
            return 1f;
        }
        return disAbsX;
    }

    private float getDisAbsY() {
        float disAbsY = Math.abs(moveY - startPosY);
        if (disAbsY < Const.MIN_SLIDE) {
            return 1f;
        }
        return disAbsY;
    }

    private double getDisZ() {
        return Math.sqrt(Math.abs(moveX - startPosX) * Math.abs(moveX - startPosX)
            + Math.abs(moveY - startPosY) * Math.abs(moveY - startPosY));
    }

    private float[] getSmallCurrentPos(float currX, float currY) {
        float[] smallCurrentPos = new float[Const.QUADRANT_2];
        if (getDisZ() < bigR) {
            smallCurrentPos[0] = currX;
            smallCurrentPos[1] = currY;
            return smallCurrentPos;
        } else {
            double disX = (getDisAbsX() * bigR) / getDisZ();
            double disY = (getDisAbsY() * bigR) / getDisZ();
            int quadrant = quadrant();
            switch (quadrant) {
                case Const.QUADRANT_1:
                    smallCurrentPos[0] = (float) (disX + startPosX - smallR);
                    smallCurrentPos[1] = (float) (startPosY - disY - smallR);
                    break;
                case Const.QUADRANT_2:
                    smallCurrentPos[0] = (float) (startPosX - disX - smallR);
                    smallCurrentPos[1] = (float) (startPosY - disY - smallR);
                    break;
                case Const.QUADRANT_3:
                    smallCurrentPos[0] = (float) (startPosX - disX - smallR);
                    smallCurrentPos[1] = (float) (disY + startPosY - smallR);
                    break;
                case Const.QUADRANT_4:
                    smallCurrentPos[0] = (float) (disX + startPosX - smallR);
                    smallCurrentPos[1] = (float) (disY + startPosY - smallR);
                    break;
                default:
                    break;
            }
        }
        return smallCurrentPos;
    }
}
