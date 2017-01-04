import java.io.*;
import java.util.*;

class Student {
    String id, first, last;
    Collection<Course> list = new ArrayList<>();
    public Student(String i, String f, String a) {
        id = i; first = f; last = a;
    }
    public String toString() { return last; }
}

class Course {
    String code, name, hour, day;
    Collection<Student> list = new ArrayList<>();
    public Course(String c, String n, String h, String d) {
        code = c; name = n; hour = h; day = d;
    }
    public String toString() { return code; }
}

class Database {
    File file;
    Map<String, Student> stdMap = new TreeMap<>();
    Map<String, Course>  crsMap = new TreeMap<>();
    Map<String, Set<Course>> rooms = new TreeMap<>();
    public Database(File f) { file = f; }
    void process(String line) {
        String[] a = line.split("\t");
        String s_id = a[0];
        Student s = stdMap.get(s_id);
        if (s == null) 
            stdMap.put(s_id, s = new Student(s_id, a[1], a[2]));
        String code = a[3];
        Course c = crsMap.get(code);
        if (c == null) 
            crsMap.put(code, c = new Course(code, a[4], a[5], a[6]));
        s.list.add(c); c.list.add(s);
        String room = a[7];
        room = room.charAt(0)+room.substring(room.indexOf('#')+1);
        Set<Course> t = rooms.get(room);
        if (t == null) 
            rooms.put(room, t = new LinkedHashSet<Course>());
        t.add(c);
    }
    public void process() throws IOException {
        System.out.println(file+" "+file.length());
        InputStream in = new FileInputStream(file);
        byte[] ba = new byte[in.available()];
        in.read(ba); in.close(); 
        String[] sa = new String(ba).split("\n");
        System.out.println(sa.length+" lines");
        for (String s : sa) { process(s); }
        System.out.println(stdMap.size()+" Students");
        System.out.println(crsMap.size()+" Courses");
        System.out.println(rooms.size()+" Rooms:");
        System.out.println(rooms.keySet()); int n = 0;
        for (String k : rooms.keySet()) n += rooms.get(k).size();
        System.out.println(n+" Courses (added in rooms)");
    }
    public void printStd(String id) {
        Student s = stdMap.get(id);
        System.out.println(s.first+" "+s.last);
        System.out.println(s.list);
    }
    public void printCrs(String code) {
        Course c = crsMap.get(code);
        System.out.println(code+" "+c.name);
        System.out.println(c.list);
    }
    public static void main(String[] args) throws IOException {
        String DATA = "C:\\GitHub\\PlainJava\\database\\Sample.txt";
        new Database(new File(DATA)).process() ;
    }
}
