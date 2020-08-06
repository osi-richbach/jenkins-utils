#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.semver.SemVer

def call(String label = '', List tagPrefixes = [], String tagPrefix = '') {
  semVer = new SemVer(this)
  tagPrefix ? semVer.newTag(label, tagPrefix) : semVer.newTag(label, tagPrefixes)
}
