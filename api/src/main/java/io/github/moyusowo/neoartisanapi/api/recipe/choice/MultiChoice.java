package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Recipe choice that combines multiple choices with AND logic.
 * <p>
 * This choice matches only when all contained choices match the given item.
 * </p>
 */
public final class MultiChoice implements Choice {
    private final List<Choice> choices;

    /**
     * Creates a new multi choice with the given choices
     *
     * @param choices the choices to combine (cannot be null)
     */
    public MultiChoice(@NotNull Collection<Choice> choices) {
        this.choices = new ArrayList<>(choices);
    }

    @Override
    public boolean matches(@Nullable ItemStack itemStack) {
        for (Choice choice : choices) {
            if (!choice.matches(itemStack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean matches(@Nullable NamespacedKey itemId) {
        for (Choice choice : choices) {
            if (!choice.matches(itemId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the unmodifiable collection of choices
     *
     * @return unmodifiable collection of choices
     */
    @Unmodifiable
    @NotNull
    public Collection<Choice> getChoices() {
        return Collections.unmodifiableCollection(choices);
    }
}
