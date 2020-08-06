package gov.ca.cwds.jenkins

class AnsibleCommandRunner {
  def rollbackOldVersionCommand(command, oldVersion) {
    command.replaceAll(/(_(NUMBER|VERSION)=)([^ ]+)/, '$1' + oldVersion)
  }
}
