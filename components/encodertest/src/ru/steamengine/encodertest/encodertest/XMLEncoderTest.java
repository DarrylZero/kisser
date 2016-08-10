package ru.steamengine.encodertest.encodertest;

import java.beans.XMLEncoder;

/**
 * Created by Steam engine corp. in 24.01.2010 20:38:34
 *
 * @author Christopher Marlowe
 */
public class XMLEncoderTest {


    public static class TestedClass {

        private byte byteProperty = 1;

        private String s = "";


        public byte getByteProperty() {
            return byteProperty;
        }

        public void setByteProperty(byte byteProperty) {
            this.byteProperty = byteProperty;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }


    public static void main(String[] args) {
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(System.out);
        TestedClass testedClass = new TestedClass();
        xmlEncoder.writeObject(testedClass);
        xmlEncoder.flush();


    }


}
