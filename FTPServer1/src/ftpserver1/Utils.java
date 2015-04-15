/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpserver1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author nikita
 */
public class Utils {
    public String getTime(final long timeInMillis)
{
    final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
    final Calendar c = Calendar.getInstance();
    c.setTimeInMillis(timeInMillis);
    c.setTimeZone(TimeZone.getDefault());
    return format.format(c.getTime());
}
}
