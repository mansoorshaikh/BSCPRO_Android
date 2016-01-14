package applicationVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import android.graphics.Typeface;

import com.MWC.chatapp.UserProfileActivity;

public class ChatSingleton {

private static ChatSingleton chatinstance=null;
public static ArrayList<String> countryArrayList=new ArrayList<String>();
public static  ArrayList<AgentListVO> SAorMDArrayList=new ArrayList<AgentListVO>();
public static ArrayList<AgentListVO> UplineCEOArrayList=new ArrayList<AgentListVO>();
public static ArrayList<AgentListVO> uplineSMDArrayList=new ArrayList<AgentListVO>();
public  ArrayList<UserProfileListVO> userprofileArrayList=new ArrayList<UserProfileListVO>();
public String UTCTIME="",UTCDATE="";
public int Deviceheight = 0, devicewidth = 0;
public int open;
public ChatSingleton()
{}

public static ChatSingleton getInstance()
{
	if(chatinstance==null)
	{
		chatinstance=new ChatSingleton();
		
	}
	return chatinstance;

}

public String getDate(String timezone)
{
	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	if(timezone.equalsIgnoreCase("PST"))
		timezone="America/Los_Angeles";
	
	df.setTimeZone(TimeZone.getTimeZone(timezone));
	
	SimpleDateFormat dfutc = new SimpleDateFormat("MM/dd/yyyy");
	dfutc.setTimeZone(TimeZone.getTimeZone("UTC"));
	UTCDATE=dfutc.format(Calendar.getInstance().getTime());
	return df.format(Calendar.getInstance().getTime());
}


public String getTime(String timezone)
{
	SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa");
	if(timezone.equalsIgnoreCase("PST"))
		timezone="America/Los_Angeles";
	
	df.setTimeZone(TimeZone.getTimeZone(timezone));
	
	SimpleDateFormat dfutc = new SimpleDateFormat("hh:mm aaa");
	dfutc.setTimeZone(TimeZone.getTimeZone("UTC"));
	UTCTIME=dfutc.format(Calendar.getInstance().getTime());
	
	return df.format(Calendar.getInstance().getTime());
	
}
	
}
