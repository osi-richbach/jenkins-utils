#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.StaticAnalyzer
import gov.ca.cwds.jenkins.docker.Docker
import gov.ca.cwds.jenkins.common.BuildMetadata


def call(rtGradle = null) {
    BuildMetadata buildMetadata = new BuildMetadata(this, this.env.JOB_NAME, this.env.BUILD_ID, this.env.WORKSPACE)
    def docker = new Docker(this)
    staticAnalyzer = new StaticAnalyzer(docker, rtGradle, this)
    staticAnalyzer.lint(buildMetadata);
}
