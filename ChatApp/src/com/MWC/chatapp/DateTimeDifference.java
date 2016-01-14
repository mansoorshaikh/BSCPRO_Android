package com.MWC.chatapp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeDifference {

	public String findDifference(String date)
	{
	
		String result=""; 
		
		String dateStart = date;
		
		if(dateStart.equals(""))
		{
			return result;
			
		}
		
		Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentdate = df.format(c.getTime());
		
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		
		
		java.util.Date d1 = null;
		java.util.Date d2 = null;

		try {
			d1 =  format.parse(dateStart);
			d2 =  format.parse(currentdate);

			//in milliseconds
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long seconds = diff / 1000;
			long minutes = seconds / 60;
			long hrs = minutes / 60;
			long days = hrs / 24;
            long month=days/30;
			long year=month/12;
			   
			 if(hrs<=1){
			       result="Recently..";
			    }
			 else if(hrs<=24){
			    	System.out.println(hrs+"  hours ago");
			    	result=hrs+"  hours ago";			 
			 }else if(hrs<=48){
			    	result="yesterday";
			    	
			    }else if(days<=30){
			    	result=days+" days ago";
			    }else if(month<12){
			    	result=month+" month ago";
	    }
			    else{
			    	result=year+" Yers ago";
			    }
			
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		 return  result;
	}
	
	
}