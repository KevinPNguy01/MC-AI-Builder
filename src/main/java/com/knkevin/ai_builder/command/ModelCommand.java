package com.knkevin.ai_builder.command;

import com.knkevin.ai_builder.command.arguments.ApplySetArgument;
import com.knkevin.ai_builder.command.arguments.ModelFileArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static com.knkevin.ai_builder.command.arguments.AxisArgument.axisArg;
import static com.knkevin.ai_builder.command.arguments.DirectionArgument.directionArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * The main class for the "model" command.
 */
public class ModelCommand {
    /**
     * Registers the "model" command and all of its subcommands.
     * @param dispatcher CommandDispatcher to register commands.
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("model")
            .then(literal("load").then(argument("filename", ModelFileArgument.modelFileArgument()).executes(LoadCommand::load)))
            .then(literal("place").executes(PlaceCommand::place))
            .then(literal("undo").executes(UndoCommand::undo))
            .then(literal("scale").then(argument("applySet", ApplySetArgument.applySetArg())
                .then(argument("scale", floatArg()).executes(ScaleCommand::scaleAll))
                .then(argument("x-scale", floatArg()).then(argument("y-scale", floatArg()).then(argument("z-scale", floatArg()).executes(ScaleCommand::scale))))
                .then(argument("axis", axisArg()).then(argument("scale", floatArg()).executes(ScaleCommand::scaleAxis)))
            ))
            .then(literal("rotate")
                .then(argument("x-angle", floatArg()).then(argument("y-angle", floatArg()).then(argument("z-angle", floatArg()).executes(RotateCommand::rotate))))
                .then(argument("axis", axisArg()).then(argument("angle", floatArg()).executes(RotateCommand::rotateAxis))
            ))
            .then(literal("move")
                .then(argument("distance", floatArg()).executes(MoveCommand::move)
                    .then(argument("direction", directionArg()).executes(MoveCommand::moveDirection))
                )
            )
            .then(literal("generate")
                .then(literal("cancel").executes(GenerateCommand::cancelGeneration))
                .then(argument("prompt", string()).executes(GenerateCommand::generateModel)
                    .then(argument("model_name", string()).executes(GenerateCommand::generateModel))
                )
            )
        );
    }

    /**
     * Runs when a command tried to execute without a loaded Model.
     * @param command The executed command.
     * @return 0
     */
    protected static int noModelLoaded(CommandContext<CommandSourceStack> command) {
        command.getSource().sendFailure(Component.literal("Error: No model has been loaded."));
        return 0;
    }
}
