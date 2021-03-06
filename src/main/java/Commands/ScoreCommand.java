package Commands;

import Database.SQLiteConnection;
import Main.DeathRollMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * DeathRoll Command: Score.
 * <ul>
 *     <li> Usable by: Registered users.
 *     <li> Alias: Score.
 *     <li> Arguments: None.
 *     <li> Purpose: Returns the score value for the user who used the command.
 * </ul>
 *
 * @author Sérgio de Aguiar (pioavenger)
 * @version 1.0.0
 * @since 1.0.0
 */
public class ScoreCommand extends ListenerAdapter
{
    /**
     * Inherited from ListenerAdapter.
     *
     * This implementation handles the Score command usage and can result in the following:
     * <ul>
     *     <li> error, due to incorrect number of arguments;
     *     <li> error, due to the calling user not being registered;
     *     <li> success, where the resulting value is displayed.
     * </ul>
     *
     * @param event The JDA event relative to a message having been read by the application in a server channel.
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event)
    {
        if (!event.getAuthor().isBot())
        {
            String[] messageText = event.getMessage().getContentRaw().split("\\s+");
            EmbedBuilder embedBuilder = new EmbedBuilder();

            if (messageText[0].equalsIgnoreCase(DeathRollMain.getPrefix() + "score"))
            {
                if(messageText.length != 1)
                {
                    embedBuilder.setColor(DeathRollMain.EMBED_FAILURE)
                            .setTitle("Incorrect number of arguments!")
                            .setDescription("The 'score' command takes no arguments." +
                                    "\nUsage: " + DeathRollMain.getPrefix() + "score");
                }
                else
                {
                    if (SQLiteConnection.isUserRegistered(event.getAuthor().getId()))
                    {
                        int userScore = SQLiteConnection.getUserScore(event.getAuthor().getId());

                        embedBuilder.setColor(DeathRollMain.EMBED_SUCCESS)
                                .setTitle("Current Score:")
                                .setDescription("User " + event.getAuthor().getAsMention() + " has a current score" +
                                        " of " + userScore + ".");
                    }
                    else
                    {
                        embedBuilder.setColor(DeathRollMain.EMBED_FAILURE)
                                .setTitle("User not registered!")
                                .setDescription("To use the 'score' command, you must be registered." +
                                        "\nTo do so, run the " + DeathRollMain.getPrefix() + "register command.");
                    }
                }
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
