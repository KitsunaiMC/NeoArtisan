package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class MultiChoice implements Choice {
    private final List<Choice> choices;

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

    @Unmodifiable
    @NotNull
    public Collection<Choice> getChoices() {
        return Collections.unmodifiableCollection(choices);
    }
}
