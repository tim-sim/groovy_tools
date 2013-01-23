import groovy.io.FileType

final PRODUCT_LOCATION = "D:\\chartis\\hg\\team_work\\claims\\claims-auto\\claims-configuration-auto\\src\\main\\resources\\product"

final TEST_PATH = "\\PREMIER_CLAIM_AU_1.0_claimsEvaluation\\Rules\\ruleSet_v1"

final TEST_RULES_FILE = "\\AutoEvaluationFeature\\base-rules.xml"

final TEST_WORKSPACE = "\\PREMIER_CLAIM_AU_1.0_claimsEvaluation"

//rulesDir = new File(PRODUCT_LOCATION + TEST_PATH)
//rulesDir.eachDir{println it.name}

//def rules = extractComponentRules(PRODUCT_LOCATION + TEST_PATH + TEST_RULES_FILE)

//rules.each {println it}

//extractWorkspaceRules(PRODUCT_LOCATION + TEST_WORKSPACE + "\\Rules").each { println it }

extractComponentRules(PRODUCT_LOCATION + TEST_WORKSPACE + "\\Rules\\ruleSet_v1\\ruleSet.xml").each { println it}

def parseCommandLine() {
}

def extractWorkspaceRules(workspace) {
    def ruleIds = []
    new File(workspace).eachFileRecurse(groovy.io.FileType.FILES) { 
        if (it =~ /.*rules\.xml/) {
            it.text.eachMatch(~/\w{8}-\w{4}-\w{4}-\w{4}-\w{12}/) {ruleIds << it}
        }
    }
    ruleIds
}

def extractComponentRules(componentFile) {
    def ruleIds = []
    new File(componentFile).text.eachMatch(~/\w{8}-\w{4}-\w{4}-\w{4}-\w{12}/) {ruleIds << it}
    ruleIds
}

class Product {
    def REGEX_RULE_ID = ~/\w{8}-\w{4}-\w{4}-\w{4}-\w{12}/
    def REGEX_RULE_FILE = /.*rules\.xml/
    def PATH_RULES = "\\Rules\\ruleSet_v1"
    def FILE_RULESET = "\\ruleSet.xml"

    def root
    def code
    def version = "1.0"
    def workspaces

    def testRules() {
    }

    def generateRuleSet() {
    }

    def fixRuleSet() {
    }

    def listWorkspaces() {
    }

    def extractRules(file) {
        def rules = []
        file.text.eachMatch(REGEX_RULE_ID) { rules << it }
        rules
    }

    def extractComponentRules(dir) {
        def rules = []
        dir.eachFileRecurse(FileType.FILES) {
            if (it =~ REGEX_RULE_FILE) { rules << extractRules(it) } 
        }
    }
   
}
