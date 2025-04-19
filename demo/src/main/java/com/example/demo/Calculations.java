package com.example.demo;

import org.apache.commons.math3.fraction.Fraction;

 class Calculations {
     private String par;
     Calculations(String par){
         this.par=par;
     }

    public static Fraction parseFraction(String s) {
        if(s.contains("/")){
            String[] sm = s.split("/");
            return new Fraction(Integer.parseInt(sm[0]), Integer.parseInt(sm[1]));
        }
        else if (s.contains(".")) {
            return new Fraction(Double.parseDouble(s));
        }
        else {
            return new Fraction(Integer.parseInt(s));
        }
    }
     public static int compareByAbsoluteValue(String a, String b) {
         Fraction f1 = parseFraction(a).abs();
         Fraction f2 = parseFraction(b).abs();
         return f1.compareTo(f2);
     }

     public static int compare(String a, String b) {
         Fraction f1 = parseFraction(a);
         Fraction f2 = parseFraction(b);
         return f1.compareTo(f2);
     }

    public String plus(String a, String b){
         if(par.equals("Обыкновенные")){
             Fraction cur = parseFraction(a).add(parseFraction(b));
             return cur.getDenominator()==1?String.valueOf(cur.getNumerator()):cur.toString().replace(" ", "");
         }
        else {
             return String.valueOf(Double.parseDouble(a)+Double.parseDouble(b));
         }
    }
    public String minus(String a, String b){
        if(par.equals("Обыкновенные")){
            Fraction cur = parseFraction(a).subtract(parseFraction(b));
            return cur.getDenominator()==1?String.valueOf(cur.getNumerator()):cur.toString().replace(" ", "");
        }
        else {
            return String.valueOf(Double.parseDouble(a)-Double.parseDouble(b));
        }

    }
     public String multiply(String a, String b){
         if(par.equals("Обыкновенные")){
             Fraction cur = parseFraction(a).multiply(parseFraction(b));
             return cur.getDenominator()==1?String.valueOf(cur.getNumerator()):cur.toString().replace(" ", "");
         }
         else {
             return String.valueOf(Double.parseDouble(a)*Double.parseDouble(b));
         }

     }
     public String divide(String a, String b){
         if(par.equals("Обыкновенные")){
             Fraction cur = parseFraction(a).divide(parseFraction(b));
             return cur.getDenominator()==1?String.valueOf(cur.getNumerator()):cur.toString().replace(" ", "");
         }
         else {
             return String.valueOf(Double.parseDouble(a)/Double.parseDouble(b));
         }

     }
}
