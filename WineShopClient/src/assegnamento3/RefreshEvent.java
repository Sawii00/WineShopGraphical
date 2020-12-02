package assegnamento3;

import javafx.event.Event;
import javafx.event.EventType;

public class RefreshEvent extends Event
{


	private static final long serialVersionUID = 5970647945895438198L;
	public static final EventType<RefreshEvent> REFRESH = new EventType<>(Event.ANY, "Refresh");
	
	public RefreshEvent(EventType<? extends Event> arg0)
	{
		super(arg0);
	}

	
}
