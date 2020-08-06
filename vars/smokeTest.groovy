#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.SmokeTester

def call(String path, String url) {
  smokeTester = new SmokeTester(this)
  smokeTester.runSmokeTest(path, url);
}
