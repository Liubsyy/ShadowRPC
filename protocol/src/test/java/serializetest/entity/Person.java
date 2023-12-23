package serializetest.entity;

import com.liubs.shadowrpc.protocol.annotation.ShadowEntity;
import com.liubs.shadowrpc.protocol.annotation.ShadowField;

import java.util.Objects;


/**
 * @author Liubsyy
 * @date 2023/12/23 2:07 PM
 */
@ShadowEntity
public class Person {
    @ShadowField(1)
    private int age;
    @ShadowField(2)
    private double height;
    @ShadowField(3)
    private float weight;
    @ShadowField(4)
    private long money;
    @ShadowField(5)
    private String name;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Double.compare(person.height, height) == 0 && Float.compare(person.weight, weight) == 0 && money == person.money && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, height, weight, money, name);
    }



}
