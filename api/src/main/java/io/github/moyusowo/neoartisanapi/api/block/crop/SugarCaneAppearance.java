package io.github.moyusowo.neoartisanapi.api.block.crop;

/**
 * 甘蔗型作物外观配置（利用原版甘蔗未使用的age值1-15）
 * <p>
 * <b>原版状态映射策略：</b>
 * <ul>
 *   <li>原版甘蔗始终使用age=0，1-15为未使用状态</li>
 *   <li>自定义作物可安全占用1-15作为不同生长阶段</li>
 *   <li>传入age=0将抛出异常（避免与原版冲突）</li>
 * </ul>
 *
 * @implNote 实际客户端显示需通过资源包覆盖原版甘蔗纹理
 */
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
