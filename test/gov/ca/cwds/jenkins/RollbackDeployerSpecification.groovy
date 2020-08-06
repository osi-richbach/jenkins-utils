package gov.ca.cwds.jenkins

import spock.lang.Specification

class RollbackDeployerSpecification extends Specification {
  class PipelineScript {
    def sh(hash) { }
    def ws(closure) { }
    def git(hash) { }
    def readYaml(hash) { }
  }

  def "#rollback with no failures"() {
    given: 'a rollback deployer'
    def pipeline = Mock(PipelineScript)
    def rollbackDeployer = new RollbackDeployer(pipeline)
    def closure = Mock(Closure)

    when: 'no failures'
    rollbackDeployer.rollback('dashboard', 'preint', 'githubCredentials', 'ansible-playbook stuff', closure)

    then: 'it checks out the current manifest version'
    1 * pipeline.ws(_ as Closure) >> { rollbackDeployer.oldVersion = '0.1.1' }

    then: 'it sets the old version in case a rollback is needed'
    rollbackDeployer.oldVersion == '0.1.1'

    then: 'it runs the passed in closure'
    1 * closure.call()
  }

  def "#rollback with a failure"() {
    given: 'a rollback deployer'
    def pipeline = Mock(PipelineScript)
    def manifestUpdater = Mock(ManifestUpdater)
    def rollbackDeployer = new RollbackDeployer(pipeline, manifestUpdater)
    def closure = Mock(Closure)

    when: 'something goes wrong'
    rollbackDeployer.rollback('dashboard', 'preint', 'githubCredentials', 'ansible-playbook stuff', closure)

    then: 'the closure is called and returns an error'
    1 * pipeline.ws(_ as Closure) >> { rollbackDeployer.oldVersion = '2.3.4' }
    1 * closure.call() >> { throw new IOException() }

    then: 'the pipeline redeploys and updates the manifest to the old version'
    1 * pipeline.ws(_ as Closure)
    1 * manifestUpdater.update('dashboard', 'preint', 'githubCredentials', '2.3.4')

    then: 'the error is rethrown to cause the build to fail'
    thrown(IOException)
  }

  def "#getOldVersionFromManifest"() {
    given: 'a rollback deployer'
    def pipeline = Mock(PipelineScript)
    def rollbackDeployer = new RollbackDeployer(pipeline)

    when: 'looking up the the old version'
    def version = rollbackDeployer.getOldVersionFromManifest('dashboard', 'integration', 'credentials')

    then: 'it checks out cares'
    1 * pipeline.git([branch: 'master', credentialsId: 'credentials', url: 'git@github.com:ca-cwds/cws-cares.git'])

    then: 'it sets the old version'
    1 * pipeline.readYaml([file: 'integration.yaml']) >> [dashboard: '1.2.4', cans: '1.4.5']
    version == '1.2.4'
  }

  def "#rollbackDeployment"() {
    given: 'a rollback deployer'
    def pipeline = Mock(PipelineScript)
    def rollbackDeployer = new RollbackDeployer(pipeline)
    rollbackDeployer.oldVersion = '0.9.9'
    def ansibleScript = 'ansible-playbook -e APP_VERSION=1.0.0 deploy-dashboard.yml'

    when: 'rolling back a deployment'
    rollbackDeployer.rollbackDeployment(ansibleScript, 'credentials')

    then: 'it checks out the ansible project'
    1 * pipeline.git([branch: 'master', credentialsId: 'credentials', url: 'git@github.com:ca-cwds/de-ansible.git'])

    then: 'it runs the ansible playbook to deploy old version'
    1 * pipeline.sh([script: 'ansible-playbook -e APP_VERSION=0.9.9 deploy-dashboard.yml'])
  }
}
