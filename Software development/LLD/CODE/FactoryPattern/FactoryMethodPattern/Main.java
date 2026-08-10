package FactoryPattern.FactoryMethodPattern;

enum ShapeType {
    CIRCLE,
    SQUARE
}

interface Shape {
    void draw();
}

class Circle implements Shape {
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}

class Square implements Shape {
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}

abstract class ShapeFactory {
    abstract Shape createShape();
}

class CircleFactory extends ShapeFactory {
    Shape createShape() {
        return new Circle();
    }
}

class SquareFactory extends ShapeFactory {
    Shape createShape() {
        return new Square();
    }
}

public class Main {
    public static void main(String[] args) {
        ShapeFactory circleFactory = new CircleFactory();
        Shape circle = circleFactory.createShape();
        circle.draw();

    }
}
