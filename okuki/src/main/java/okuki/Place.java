package okuki;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Place<T> {

    private volatile int hashCode = 0;

    private static final Map<Class<? extends Place>, List<Class<? extends Place>>> hierarchies = new HashMap<>();

    private final T data;

    public Place(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public List<Class<? extends Place>> getHierarchy() {
        return getHierarchyForPlace(getClass());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass().equals(obj.getClass())) {
            Place p = (Place) (obj);
            if (data == null) {
                return p.getData() == null;
            } else {
                return data.equals(p.getData());
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            int result = 17;
            result = 37 * result + (data == null ? 0 : data.hashCode());
            hashCode = result;
        }
        return hashCode;
    }

    public static List<Class<? extends Place>> getHierarchyForPlace(Class<? extends Place> placeClass) {
        if (!hierarchies.containsKey(placeClass)) {
            LinkedList<Class<? extends Place>> hierarchList = new LinkedList<>();
            Class<? extends Place> clazz = placeClass;
            while (clazz != null) {
                if (hierarchList.contains(clazz)) {
                    throw new RuntimeException("Okuki error: Circular place hierarchy found for " + placeClass.getName());
                }
                hierarchList.addFirst(clazz);
                PlaceConfig config = clazz.getAnnotation(PlaceConfig.class);
                clazz = ((config == null) || NullPlace.class.equals(config.parent()))
                        ? null : config.parent();
            }
            hierarchies.put(placeClass, hierarchList);
        }
        return hierarchies.get(placeClass);
    }

    @Override
    public String toString() {
        return String.format("%s{data=%s}", this.getClass().getSimpleName(), data);
    }
}
