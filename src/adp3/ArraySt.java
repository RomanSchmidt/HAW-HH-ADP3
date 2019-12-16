package adp3;

import java.util.ArrayList;
import java.util.Arrays;

class ArrayST<Key, Val> {
    private static final int MAX_SIZE = 1;
    private Val[] values;
    private Key[] keys;
    private int size = 0;

    public ArrayST() {
        keys = (Key[]) new Object[MAX_SIZE];
        values = (Val[]) new Object[MAX_SIZE];
    }

    /**
     * eine main zum testen
     ***************************/
    public static void main(String[] args) {
        ArrayST<Integer, Integer> test = new ArrayST<>();
        for (int i = 1, k = 2; i <= 10; i++, k *= 2) {
            test.put(i, k);
        }
        test.get(5);
        test.get(6);
        test.print();
        System.out.println("Schlüssel: " + test.keys());
        System.out.println("Laenge: " + test.size());

        ArrayST<String, Integer> st;
        st = new ArrayST<>();
        String[] str = new String[]{"s", "e", "a", "r", "c", "h", "e", "x", "a", "m", "p", "l", "e"};
        for (int i = 0; i < str.length; i++) {
            st.put(str[i], i);
        }
        for (String key : st.keys()) {
            System.out.printf("\n%s->%d ", key, st.get(key));
        }
    }

    //Fügt ein Schlüssel-Wert-Paar in die Symboltabelle ein, bzw.
    //    entfernt das Paar, wenn der Wert null ist.
    public void put(Key k, Val v) {

        // value == null -> delete key
        if (v == null) {
            for (int i = 0; i < size; i++) {
                if (k.equals(keys[i])) {
                    keys[i] = keys[size - 1];
                    values[i] = values[size - 1];
                    keys[size - 1] = null;
                    values[size - 1] = null;
                    size--;
                    if (size <= keys.length / 4)
                        resize(size / 2);
                }
            }
        }

        // key bereits vorhanden -> überschreiben
        else if (contains(k)) {
            for (int i = 0; i < size; i++) {
                if (k.equals(keys[i])) {
                    rearrange(i);
                    values[0] = v;
                    break;
                }
            }
        }

        // ansonsten key und value hinten anfügen
        else {
            if (size >= keys.length)
                resize(size * 2);
            values[size] = v;
            keys[size] = k;
            rearrange(size);
            size++;
        }
    }

    // Liefert den Wert zu einem Schlüssel oder null, wenn der
    //    Schlüssel nicht enthalten ist.
    public Val get(Key k) {
        for (int i = 0; i < size; i++) {
            if (k.equals(keys[i])) {
                rearrange(i);
                return values[0];
            }
        }
        return null;
    }

    //Löscht eine Schlüssel-Wert-Paar.
    //Implementierung: put(key,null)
    public void delete(Key k) {
        put(k, null);
    }

    //Gibt es einen Wert zu Schlüssel key?
    //    Implementierung: return get(key)!=null
    public boolean contains(Key k) {
        return get(k) != null;
    }

    //Ist die Tabelle leer?
    //    Implementierung: return size()==0
    public boolean isEmpty() {
        return size() == 0;
    }

    //Größe der Tabelle
    public int size() {
        return size;
    }

    // Liefert alle Schlüssel der Tabelle als iterierbare Sammlung zurück.
    public Iterable<Key> keys() {
        return new ArrayList<>(Arrays.asList(keys).subList(0, size));
    }

    // größe anpassen, wenn array zu klein ist
    private void resize(int newSize) {
        Key[] tmpkey = (Key[]) new Object[newSize];
        Val[] tmpval = (Val[]) new Object[newSize];
        System.arraycopy(keys, 0, tmpkey, 0, size);
        System.arraycopy(values, 0, tmpval, 0, size);
        keys = tmpkey;
        values = tmpval;
    }

    // --- Aufgabe 3.2 ---
    // arrays neu anordnen
    private void rearrange(int position) {
        Val tmpVal = values[position];
        Key tmpKey = keys[position];
        System.arraycopy(keys, 0, keys, 1, position);
        System.arraycopy(values, 0, values, 1, position);
        keys[0] = tmpKey;
        values[0] = tmpVal;
    }

    public void print() {
        for (int i = 0; i < size; i++) {
            System.out.println("Key: " + keys[i] + ", Value: " + values[i]);
        }
    }
}
