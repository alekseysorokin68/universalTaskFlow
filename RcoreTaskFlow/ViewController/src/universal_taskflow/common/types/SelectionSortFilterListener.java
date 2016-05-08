package universal_taskflow.common.types;

import java.util.Map;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.event.SortEvent;

public interface SelectionSortFilterListener
{
  void processEvent(String taskFlowId, 
                    SelectionEvent selectionEvent, 
                    SortEvent sortEvent,
                    Map<String,Object> row
  );
}
