package etec.com.br;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class TextToMorse {
    public static final double SHORT = 0.2;
    public static final double LONG = SHORT * 3;
    public static final double BREAK = SHORT * 2;

    static public class Item {
        public double _time;
        public boolean _light;

        public Item(double time, boolean light) {
            _time = time;
            _light = light;
        }
    }

    static public class Letter {
        public Queue<Item> _items;

        public Letter(){
            _items = new LinkedList<Item>();
        }

        public Letter(Queue<Item> items) {
            this._items = items;
        }

        public Letter(Letter l) {
            _items = new LinkedList<Item>();

            for(Item i : l._items){
                _items.add(new Item(i._time, i._light));
            }
        }

        public void addItem(Item i) {
            _items.add(i);
        }
    }

    public static MorseAlphabet morseAlphabet;
    public static HashMap<String, Letter> dictionary;

    static {
        morseAlphabet = new MorseAlphabet();
        dictionary = new HashMap<String, Letter>();
        Field[] allFields = MorseAlphabet.class.getDeclaredFields();
        Double[] sequence;

        for(Field f : allFields){
            try {
                Letter l = new Letter();
                sequence = (Double[]) f.get(morseAlphabet);
                for(double d : sequence) {
                    Item i = new Item(d, true);
                    l.addItem(i);
                    l.addItem(new Item(BREAK, false));
                }
                dictionary.put(f.getName(), l);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Letter getLetter(String s) {
        return new Letter(dictionary.get(s));
    }
}
