package com.example.somaggiainfesta;

import com.example.somaggiainfesta.fragments.ActiveCommandsFragment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KitchenUnitTest {
    Kitchen k;

    @Before
    public void init(){
        k = new Kitchen();

    }

    //TODO test per verificare che il fragment Congratulations sia caricato solo al momento opportuno. Spostare in AndroidTest ?
    @Test
    public void congratulationsFragmentTest(){
        System.out.println(k.actualFrag);
        assertTrue(k.actualFrag instanceof ActiveCommandsFragment);

    }

}