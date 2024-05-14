package CollectionFramework;

public class TestHashMap {
    public static void main(String[] args) {
        MyHashMap<Object, Object> hmap = new MyHashMap<>();

        hmap.put("first", "tomato");
        hmap.put(1, "milk");
        hmap.put(3, "bread");
        hmap.put("second", "cucumber");
        hmap.put(3, "beer");

        System.out.println(hmap.getSize());
        System.out.println(hmap.get(1));
        System.out.println(hmap.get("second"));
        hmap.remove(3);
        System.out.println(hmap.get(3));
        System.out.println(hmap.getSize());
        System.out.println(hmap.containsKey("second"));

    }
}
