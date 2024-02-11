package com.example.chatjava.model;

import java.io.Serializable;

public class Message  implements Serializable {

   private String user;
   private String text;


   public Message(String user, String text) {
      this.user = user;
      this.text = text;
   }


   public String getUser() {
      return user;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public String toString() {
      return
              user + '\'' +
              "-->" + text ;
   }


}
