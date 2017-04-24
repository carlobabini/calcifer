package it.angelobabini.calcifer.samples.crud;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { … }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class ProductFormDesign extends CssLayout {
    protected TextField productName;
    protected TextField price;
    protected NumberField stockCount;
    protected ComboBox availability;
    protected CategoryField category;
    protected Button save;
    protected Button cancel;
    protected Button delete;

    public ProductFormDesign() {
        Design.read(this);
    }
}