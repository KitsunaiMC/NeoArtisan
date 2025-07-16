package io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.crop;

import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.CropAppearance;

public class SugarCaneAppearance implements CropAppearance {
    private final int age;

    public SugarCaneAppearance(int age) {
        if (age < 1 || age >= 15) throw new IllegalArgumentException();
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
