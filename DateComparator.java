package basePackage;
import java.util.Comparator;
public class DateComparator implements Comparator<Date> {
    public int compare(Date temp0, Date temp1) {
        if (temp0.isAfter(temp1))
             return 1;
         else if(temp1.isAfter(temp0))
             return -1;
         return 0;
     }

}
