package io.github.moyusowo.neoartisan.item;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
final class ItemCommandRegistrar {
    private static final SuggestionProvider<CommandSourceStack> REGISTRY_ID_SUGGESTIONS =
            (ctx, builder) -> {
                String currentInput = builder.getRemaining().toLowerCase();
                Registries.ITEM.getAllIds().stream()
                        .filter(id -> id.asString().startsWith(currentInput) || id.getKey().contains(currentInput))
                        .forEach(id -> builder.suggest(id.asString()));
                return builder.buildFuture();
            };

    private static final LiteralArgumentBuilder<CommandSourceStack> command =
            Commands.literal("neoartisan").then(
                    Commands.literal("give").requires(ctx -> ctx.getSender().isOp())
                            .then(
                                    Commands.argument("registryId", ArgumentTypes.namespacedKey())
                                            .suggests(REGISTRY_ID_SUGGESTIONS)
                                            .executes(
                                                    ctx -> {
                                                        if (ctx.getSource().getSender() instanceof Player player) {
                                                            NamespacedKey registryId = ctx.getArgument("registryId", NamespacedKey.class);
                                                            if (Registries.ITEM.isArtisanItem(registryId)) {
                                                                player.give(Registries.ITEM.getArtisanItem(registryId).getItemStack());
                                                            } else {
                                                                player.sendMessage(Component.text("该物品ID不存在！").color(TextColor.color(255, 0, 0)));
                                                            }
                                                            return 1;
                                                        }
                                                        ctx.getSource().getSender().sendMessage(
                                                                Component.text("你必须是一名玩家！").color(TextColor.color(255, 0, 0))
                                                        );
                                                        return 0;
                                                    }
                                            )
                            )
                            .then(
                                    Commands.argument("registryId", ArgumentTypes.namespacedKey()).then(
                                            Commands.argument("count", IntegerArgumentType.integer(1))
                                                    .executes(
                                                            ctx -> {
                                                                if (ctx.getSource().getSender() instanceof Player player) {
                                                                    NamespacedKey registryId = ctx.getArgument("registryId", NamespacedKey.class);
                                                                    int count = IntegerArgumentType.getInteger(ctx, "count");
                                                                    if (Registries.ITEM.isArtisanItem(registryId)) {
                                                                        player.give(Registries.ITEM.getArtisanItem(registryId).getItemStack(count));
                                                                    } else {
                                                                        player.sendMessage(Component.text("该物品ID不存在！").color(TextColor.color(255, 0, 0)));
                                                                    }
                                                                    return 1;
                                                                }
                                                                ctx.getSource().getSender().sendMessage(
                                                                        Component.text("你必须是一名玩家！").color(TextColor.color(255, 0, 0))
                                                                );
                                                                return 0;
                                                            }
                                                    )
                                    )
                            )
                            .then(
                                    Commands.argument("registryId", ArgumentTypes.namespacedKey()).then(
                                            Commands.argument("player", ArgumentTypes.player())
                                                    .executes(
                                                            ctx -> {
                                                                final NamespacedKey registryId = ctx.getArgument("registryId", NamespacedKey.class);
                                                                final Player player = ctx.getArgument("player", Player.class);
                                                                if (Registries.ITEM.isArtisanItem(registryId)) {
                                                                    player.give(Registries.ITEM.getArtisanItem(registryId).getItemStack());
                                                                } else {
                                                                    player.sendMessage(Component.text("该物品ID不存在！").color(TextColor.color(255, 0, 0)));
                                                                }
                                                                return 1;
                                                            }
                                                    )
                                    )
                            )
                            .then(
                                    Commands.argument("registryId", ArgumentTypes.namespacedKey()).then(
                                            Commands.argument("player", ArgumentTypes.player()).then(
                                                    Commands.argument("count", IntegerArgumentType.integer(1))
                                                            .executes(
                                                                    ctx -> {
                                                                        final NamespacedKey registryId = ctx.getArgument("registryId", NamespacedKey.class);
                                                                        final Player player = ctx.getArgument("player", Player.class);
                                                                        final int count = ctx.getArgument("count", int.class);
                                                                        if (Registries.ITEM.isArtisanItem(registryId)) {
                                                                            player.give(Registries.ITEM.getArtisanItem(registryId).getItemStack(count));
                                                                        } else {
                                                                            player.sendMessage(Component.text("该物品ID不存在！").color(TextColor.color(255, 0, 0)));
                                                                        }
                                                                        return 1;
                                                                    }
                                                            )
                                            )
                                    )
                            )
            ).then(
                    Commands.literal("guide").executes(
                            ctx -> {
                                if (ctx.getSource().getSender() instanceof Player player) {
                                    player.give(Registries.GUIDE.getGuideBook(NeoArtisan.instance()));
                                } else {
                                    ctx.getSource().getSender().sendMessage(
                                            Component.text("你必须是一名玩家！").color(TextColor.color(255, 0, 0))
                                    );
                                }
                                return 1;
                            }
                    )
            );
    private static final LiteralCommandNode<CommandSourceStack> buildCommand = command.build();

    private ItemCommandRegistrar() {}

    @InitMethod(priority = InitPriority.COMMANDS)
    public static void registerCommands() {
        NeoArtisan.instance().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(buildCommand, List.of("na")));
    }
}
