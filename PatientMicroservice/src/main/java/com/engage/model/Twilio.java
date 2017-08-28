package com.engage.model;

public class Twilio {
private String From;
private String Body;
public String getFrom() {
	return From;
}
public void setFrom(String from) {
	From = from;
}
public String getBody() {
	return Body;
}
public void setBody(String body) {
	Body = body;
}
@Override
public String toString() {
	return "Twilio [From=" + From + ", Body=" + Body + "]";
}

}
