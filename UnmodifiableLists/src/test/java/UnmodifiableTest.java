import com.google.common.collect.ImmutableList;
import org.junit.Test;
import ovh.devolution.A;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnmodifiableTest {
    public List<A> regularList;

    public UnmodifiableTest() {
        regularList = createTestList();
    }

    private List<A> createTestList() {
        return Stream.of(
            new A("one", 1),
            new A("two", 2),
            new A("three", 3))
            .collect(Collectors.toList());
    }

    private void modifyTestList(List<A> list) {
        list.add(new A("four", 4));
        list.get(0).setText("hundred");
        list.get(0).setNumber(100);
    }

    @Test
    public void testUnmodifiable() {
        List<A> unmodifiableList =
                java.util.Collections.unmodifiableList(regularList);
        // Check that the call on UnmodifiableList will raise the UnsupportedOperationException
        assertThatThrownBy(() -> unmodifiableList.add(new A("hundred", 100)))
                .isInstanceOf(UnsupportedOperationException.class);

        modifyTestList(regularList);

        // Check that the unmodifiableList view was still affected by the modifications of the regularList
        assertThat(unmodifiableList.get(0).getNumber()).isEqualTo(100);
        assertThat(unmodifiableList.size()).isEqualTo(4);

        // Check that it's a shallow copy, which means that both lists are referencing the same object
        assertThat(unmodifiableList.get(0)).isSameAs(regularList.get(0));
    }

    @Test
    public void testImmutable() {
        List<A> immutableList = ImmutableList.copyOf(regularList);

        assertThatThrownBy(() -> immutableList.add(new A("hundred", 100)))
                .isInstanceOf(UnsupportedOperationException.class);

        modifyTestList(regularList);

        // Check that the unmodifiableList view was still affected by the modifications of the regularList
        assertThat(immutableList.get(0).getNumber()).isEqualTo(100);
        assertThat(immutableList.size()).isEqualTo(3);

        // Check that it's a shallow copy, which means that both lists are referencing the same object
        assertThat(immutableList.get(0)).isSameAs(regularList.get(0));
    }

    @Test
    public void testShallowCopy() {
        // Option 1 : Copy constructor
        List<A> shallowCopy = new ArrayList<>(regularList);

        assertThat(shallowCopy.size()).isEqualTo(3);
        assertThat(shallowCopy.get(0)).isSameAs(regularList.get(0));

        // Option 2 : List.addAll
        shallowCopy = new ArrayList<>();
        assertThat(shallowCopy.size()).isEqualTo(0);

        shallowCopy.addAll(regularList);
        assertThat(shallowCopy.size()).isEqualTo(3);
        assertThat(shallowCopy.get(0)).isSameAs(regularList.get(0));
    }

    @Test
    public void testDeepCopy() {
        List<A> deepCopy = new ArrayList<>();
        regularList.forEach((element) -> deepCopy.add(new A(element)));

        // Check that it's a deep copy, which means that both lists have references to different objects
        assertThat(deepCopy.get(0)).isNotSameAs(regularList.get(0));
        assertThat(deepCopy.size()).isEqualTo(3);

        modifyTestList(regularList);

        // Check that the deepCopy was not affected by the modifications in the regularList
        assertThat(regularList.get(0).getNumber()).isEqualTo(100);
        assertThat(deepCopy.get(0).getNumber()).isEqualTo(1);
        assertThat(deepCopy.size()).isEqualTo(3);
    }
}
