#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.RollbackDeployer

def call(applicationName, environment, githubCredentials, ansibleDeploymentScript, closure) {
  rollbackDeployer = new RollbackDeployer(this)
  rollbackDeployer.rollback(applicationName, environment, githubCredentials, ansibleDeploymentScript, closure)
}
