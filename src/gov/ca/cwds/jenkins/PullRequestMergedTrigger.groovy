package gov.ca.cwds.jenkins

class PullRequestMergedTrigger {
  def triggerProperties(tokenParameter) {
    [$class: 'GenericTrigger',
      genericVariables: [
        [key: 'pull_request_action', value: 'action'],
        [key: 'pull_request_merged', value: 'pull_request.merged'],
        [key: 'pull_request_event', value: 'pull_request'],
      ],
      token: "$tokenParameter",
      regexpFilterText: '$pull_request_action:$pull_request_merged',
      regexpFilterExpression: '^closed:true$',
      causeString: 'Triggered by a PR merge',
      printContributedVariables: false,
    ]
  }
}
