package io.github.moyusowo.neoartisanapi.api.block.crop;

public class SugarCaneAppearance implements CropAppearance {

    private final int age;

    SugarCaneAppearance(int age) {
        if (age < 1 || age >= 15) throw new IllegalArgumentException();
        this.age = age;
    }

    public int get() {
        return age;
    }

}
