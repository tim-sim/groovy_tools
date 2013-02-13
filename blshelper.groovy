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

//extractComponentRules(PRODUCT_LOCATION + TEST_WORKSPACE + "\\Rules\\ruleSet_v1\\ruleSet.xml").each { println it}

parseCommandLine(args)

def parseCommandLine(args) {
    def cli = new CliBuilder(usage: 'blshelper -[fght] [product_dir]', header: 'Options:') 
    cli.with {
        h longOpt: 'help', 'Show usage information'
        f longOpt: 'fix', 'Try to fix BLS errors'
        g longOpt: 'generate', 'Re-generate rule set files'
        t longOpt: 'test', 'Check BLS rules for consistency'
    }
    def options = cli.parse(args)
    if (!options) {
        println 'Unknown error' 
    } else if (options.h) {
        println cli.usage() 
    } else if (options.t) {
        println 'Testing..'
        def productDir = '.'
        def product = new Product(root: productDir)
        product.test()
        if (!product.errors) {
            product.errors.each { println it }
        } else {
            println 'OK'
        }
    } else {
        println cli.usage()
    }
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
    def WORKSPACE_LIST = ['claimsEvaluation', 'claimsSummaryInfo']

    def root
    def errors = []

    def test() {
        getRuleDirs().each {
            def ruleset = extractRules(it + FILE_RULESET)
            def rules = extractComponentRules(it)
            compareRules(ruleset, rules)
        }
    }
    
    def compareRules(ruleSet, rules) {
        ruleSet.each {
            if (!rules.contains(it)) {
                errors << 'Component rule is not exist in ruleset: ' + it 
            }
        }
        rules.each {
            if (!ruleSet.contains(it)) {
                errors << 'Ruleset contains non-existent rule: ' + it
            }
        }
    }

    def generateRuleSet() {
    }

    def fixRuleSet() {
    }

    def getRuleDirs() {
        def dirs = []
        new File(root).eachDir { dirs << root + "\\" + it.name + PATH_RULES }
        dirs
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
