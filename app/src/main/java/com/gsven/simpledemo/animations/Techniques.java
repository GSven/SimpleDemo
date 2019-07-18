package com.gsven.simpledemo.animations;

public enum Techniques {

    BounceInDown(BounceInDownAnimator.class),
    Pulse(PulseAnimator.class);


    private Class animatorClazz;

    Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
