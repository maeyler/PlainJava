//6.1.2004  M A Eyler

import java.util.*;
import java.awt.Event;

class ArraySet extends ArrayList implements Set {}
interface EventSet extends Set, Iterator {
   Event nextEvent();
}
class EventSetImpl extends ArraySet implements EventSet {
   Iterator current;
   EventSetImpl(Collection c) {
      addAll(c); current = iterator();
   }
   public boolean hasNext() { //have more events?
      return current.hasNext();
   }
   public Object next() { //delegate to current 
      return current.next();
   }
   public Event nextEvent() { //type-cast next()
      return (Event)next();
   }
   public void remove() { //unsupported operation
      throw new UnsupportedOperationException("remove");
   }
}

class TestSet {
   static void process(Event e) {
      //System.out.println(e);
   }
   static void process(EventSet s) {
      while (s.hasNext()) process(s.nextEvent());
   }
   public static void main(String[] args) {
      ArraySet s = new ArraySet();
      s.add("A"); s.add("A"); 
      System.out.println(s);
      Object x = s.remove(0);
      System.out.println(x);
      process(new EventSetImpl(s));
   }
}
