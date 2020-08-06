package gov.ca.cwds.jenkins.pr

import gov.ca.cwds.jenkins.semver.IncrementTypes

/**
 * See https://www.conventionalcommits.org/en/
 */
enum CommitTypeGroups {
  BREAKING_CHANGES(IncrementTypes.MAJOR, ['BREAKING CHANGE']),
  FEATURES(IncrementTypes.MINOR, ['feat', 'feature']),
  FIXES(IncrementTypes.PATCH, ['fix', 'perf']),
  OTHERS(IncrementTypes.PATCH, ['ci', 'chore', 'docs', 'refactor', 'revert', 'style', 'test'])

  IncrementTypes incrementType
  List<String> conventionalCommitTypes

  private CommitTypeGroups(IncrementTypes incrementType, List<String> conventionalCommitTypes) {
    this.incrementType = incrementType
    this.conventionalCommitTypes = conventionalCommitTypes
  }
}
