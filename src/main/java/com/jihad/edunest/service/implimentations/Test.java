package com.jihad.edunest.service.implimentations;

public record Test(String name, int age) {
    public Test {
        System.out.println("heey");
    }

    public Test(int age){
        this("fff",age);
    }
}
