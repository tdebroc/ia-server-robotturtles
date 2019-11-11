package com.grooptown.snorkunking.cucumber.stepdefs;

import com.grooptown.snorkunking.IaserversnorkunkingApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = IaserversnorkunkingApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
