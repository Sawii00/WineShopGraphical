package assegnamento3.src;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * The class {@code RefreshEvent} represents a refresh request.
 * <p>
 * It is employed by the SingleWineController to notify the CustomerController
 * that it is being closed and that it should refresh the list of wines in case
 * the wine was bought and a different amount should be displayed.
 **/
public class RefreshEvent extends Event
{

	private static final long serialVersionUID = 5970647945895438198L;
	public static final EventType<RefreshEvent> REFRESH = new EventType<>(Event.ANY, "Refresh");

	/**
	 * Class constructor.
	 **/
	public RefreshEvent(EventType<? extends Event> arg0)
	{
		super(arg0);
	}

}
