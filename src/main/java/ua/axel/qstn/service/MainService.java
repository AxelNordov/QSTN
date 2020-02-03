package ua.axel.qstn.service;

import java.util.List;

public class MainService {

    static <T> void swapElementsFromConcretePlaces(List<T> arrayList, int i, int j) {
        T temp = arrayList.get(i);
        arrayList.set(i, arrayList.get(j));
        arrayList.set(j, temp);
    }

}
