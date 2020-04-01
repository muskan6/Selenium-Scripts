require('chromedriver');
const {Builder, Key, By, until} = require('selenium-webdriver');
var webdriver = require('selenium-webdriver');
var browser = new Builder().withCapabilities(webdriver.Capabilities.chrome()).build();
const TIMEOUT = 20000;
browser.get("https://www.pepcoding.com/login");
var username = "sumeet.malik1188@gmail.com";
var password = "111111";
var srcBatchName = "Test Selenium Source";
var srcModuleName = "Module 1";
var srcLectureName = "Lecture 1";
var srcLink = "";
var destBatchName = "Test Selenium Destination";
var destModuleName = "Module 1";
var destLectureName = "Lecture patterns";
var destLink = "";
var srcArray = [];
var destArray = [];
var index = 0;

function login() {
    var userEntry = browser.findElement(By.xpath("/html/body/div[1]/div/div/div/div/div[1]/form/div[1]/input[1]")).sendKeys(username);
    userEntry.then(function () {
        var passwordEntry = browser.findElement(By.css("#login > form > div:nth-child(5) > input")).sendKeys(password);
        passwordEntry.then(function loginButtonClick() {
            var loginClick = browser.findElement(By.css("#login > form > div:nth-child(7) > button")).click();
            loginClick.then(async function () {
                console.log("login successful");
                //Now you need to find the source batch in the batches list
                //var start = performance.now();
                await findBatchData(srcBatchName, srcModuleName);
                //var end = performance.now();
                //console.log(end - start);
            }, function () {
                console.log("login unsuccessful");
            })
        })
    })
}

function findBatchData1(batchName, moduleName) {
    console.log(batchName + " " + moduleName);
}

function findBatchData(batchName, moduleName) {
    console.log()
    //Now go to resources page to access batches
    browser.get("https://www.pepcoding.com/resources/");
    browser.findElements(By.css(".course-tile.card.searchRow")).then(function iterate(allBatches) {
        allBatches.forEach((batch) => {
            batch.findElement(By.css("a > form > div > div.col.l5.s7.m7.offset-s1.offset-m1 > h2")).getText().then(function (text) {
                if (text == batchName) {
                    batch.click().then(function findModule() {
                        browser.findElements(By.css(".tabs.lecture-tabs-ul > li")).then(function (modules) {
                            modules.forEach((module) => {
                                module.findElement(By.css("a > div")).getText().then(function (mtext) {
                                    if (mtext == moduleName) {
                                        console.log(mtext);
                                        module.findElement(By.css("a")).getAttribute("href").then(async function (link) {
                                            if (batchName == srcBatchName) {
                                                srcLink = link;
                                                console.log(srcLink + " > ");
                                                await processModuleLink(srcLink, srcLectureName, batchName);
                                            } else {
                                                destLink = link;
                                                console.log(destLink + " < ");
                                                await processModuleLink(destLink, destLectureName, batchName);
                                            }
                                        }, function () {

                                        })
                                    }
                                }, function () {

                                })
                            })
                        }, function () {

                        })
                    }, function () {

                    })
                }
            })
        })
    });
}

function processModuleLink(link, lectureName, batchName) {
    browser.get(link);
    for (var i = link.length - 1; i >= 0; i--) {
        if (link[i] == '#') {
            break;
        }
    }
    var id = link.substr(i);
    browser.findElements(By.css(id + " > div > ul > a")).then(function (lectures) {
        lectures.forEach((lecture) => {
            lecture.findElement(By.css("li > p")).getText().then(function (lText) {
                if (lText == lectureName) {
                    //console.log(lText);
                    lecture.getAttribute("href").then(function (link) {
                        console.log("lecture link ", link);
                        processLectureLink(link, batchName);
                    }, function () {

                    })
                }
            }, function () {

            })
        })
    }, function () {

    })
}

class LectureValues {
    constructor(name, marks, index) {
        this._name = name;
        this._marks = marks;
        this._index = index;
    }

    get name() {
        return this._name;
    }

    get marks() {
        return this._marks;
    }

    get index() {
        return this._index;
    }
}

async function processLectureLink(link, batchName) {
    await browser.get(link);
    //go to admin
    await browser.findElement(By.css("#resourceNavTabs > li:nth-child(3) > a")).click().then(async function () {
        await browser.findElements(By.css("#resourceList > tr")).then(async function (questions) {
            await questions.forEach(question => {
                var name, marks, index;
                question.findElements(By.css("td")).then(async function (tds) {
                    await tds[0].getText().then(function (text) {
                        name = text;
                    })
                    await tds[1].findElement(By.css("input")).getAttribute("value").then(function (mark) {
                        marks = mark;
                    })
                    await tds[6].findElement(By.css("input")).getAttribute("value").then(function (ind) {
                        index = ind;
                    })
                    const obj = new LectureValues(name, marks, index);
                    if (batchName == srcBatchName) {
                        await srcArray.push(obj);
                        if (srcArray.length == questions.length) {
                            console.log(srcArray);
                            await findBatchData(destBatchName, destModuleName);
                        }
                    } else {
                        await destArray.push(obj);
                        if (destArray.length == questions.length) {
                            console.log(destArray);
                            browser.findElement(By.css("#admin > div > div:nth-child(2) > label:nth-child(3) > input")).click().then(function () {
                                browser.findElement(By.css("#mapResource > div:nth-child(1) > div > label:nth-child(2) > input")).click().then(function () {
                                    map(srcArray.length - 1).then(function () {

                                    }, function () {

                                    });
                                })
                            });
                        }
                    }
                })
            })
        });
    })
}

function getIndexFromSrcArray(index) {
    for(var i=0; i<srcArray.length; i++){
        if(destArray[index].name == srcArray[i].name){
            return i;
        }
    }
    return -1;
}

async function f(index) {
    console.log("updating for "+ index);
    var element = destArray[index];
    var fullid = "", id = 0;
    await browser.findElement(By.id("searchExistingResource")).sendKeys(Key.chord(Key.CONTROL, "a")).then(function () {
        browser.findElement(By.id("searchExistingResource")).sendKeys(element.name).then(function () {
            setTimeout(async function () {
                await browser.findElements(By.css("#resourceList > tr")).then(function (rows) {
                    rows.forEach(row => {
                        row.findElement(By.css("td")).getText().then(async function (text) {
                            if(text == element.name){
                                await row.getAttribute("id").then(function (resId) {
                                    fullid = resId;
                                    id = resId.substr(11);
                                });
                                var sIndex = await getIndexFromSrcArray(index);
                                if(sIndex == -1){
                                    if(index < destArray.length-1){
                                        index++;
                                        f(index).then();
                                    }
                                }
                                await browser.findElement(By.id("marks" + id)).sendKeys(Key.chord(Key.CONTROL, "a"), srcArray[sIndex].marks).then(function () {
                                    browser.findElement(By.id("index" + id)).sendKeys(Key.chord(Key.CONTROL, "a"), srcArray[sIndex].index).then(function () {
                                        setTimeout(function () {
                                            browser.findElement(By.css("#admin > div > div:nth-child(2) > label:nth-child(1) > span")).click().then(function () {
                                                browser.findElement(By.css("#"+fullid+" > td:nth-child(8)  > input")).click().then(async function () {
                                                    await setTimeout(function () {
                                                        browser.switchTo().alert().accept();
                                                    }, 2000);
                                                    if(index < destArray.length-1){
                                                        index++;
                                                        f(index).then();
                                                    }
                                                })
                                            })
                                        }, 2000);
                                    })
                                })
                            }
                        })
                    })
                })
            }, 3000);
        });
    })
}

async function updateDestArray() {
    if(destArray.length == 0){
        return;
    }
    console.log("now update dest");
    await browser.findElement(By.css("#admin > div > div:nth-child(2) > label:nth-child(1) > input")).click().then(async function () {
        //setTimeout(async function () {
        await browser.findElement(By.id("searchExistingResource")).click().then(async function () {
            async function findInd() {
                for(var i=0; i<destArray.length; i++){
                    var srcIndex = await getIndexFromSrcArray(i);
                    if(srcIndex == -1){
                        continue;
                    }
                    if(srcArray[srcIndex].marks != destArray[i].marks || srcArray[srcIndex].index != destArray[i].index){
                        return i;
                    }
                }
            }
            var startIndex = await findInd();
            await f(startIndex).then();
        }, function () {

        })
        //}, 1000)
    })
}

function checkInDestArray(index) {
    for (var i = 0; i < destArray.length; i++) {
        if (srcArray[index].name == destArray[i].name) {
            return false;
        }
    }
    return true;
}

async function map(index) {
    //browser.findElement(By.id("searchDbResource")).clear().then();
    var process = await checkInDestArray(index);
    if (process) {
        browser.findElement(By.id("searchDbResource")).sendKeys(Key.chord(Key.CONTROL, "a")).then(function () {
            browser.findElement(By.id("searchDbResource")).sendKeys(srcArray[index].name).then();
            setTimeout(function () {
                browser.findElement(By.css("#admin > div > div:nth-child(2) > label:nth-child(3) > span")).click().then(function () {
                    setTimeout(async function () {
                        var id = 0;
                        await browser.findElement(By.css(".mapResourceRow")).getAttribute("id").then(function (resId) {
                            id = resId;
                        });
                        await browser.findElement(By.id("marks" + id)).sendKeys(srcArray[index].marks).then(function () {
                            browser.findElement(By.id("index" + id)).sendKeys(srcArray[index].index).then(function () {
                                browser.findElement(By.name("actionButton")).click().then(async function () {
                                    await setTimeout(function () {
                                        browser.switchTo().alert().accept();
                                        /*browser.switchTo().alert().thenCatch(function (e) {
                                            // 27 maps to bot.ErrorCode.NO_MODAL_DIALOG_OPEN: http://selenium.googlecode.com/git/docs/api/javascript/source/lib/atoms/error.js.src.html#l31
                                            if (e.code !== 27) { browser.alert().accept() }
                                        })
                                            .then(function (alert) {
                                                if (alert) { browser.alert().dismiss() }
                                            });*/
                                    }, 1000);
                                    if (index > 0) {
                                        index--;
                                        map(index).then();
                                    } else {
                                        updateDestArray().then();
                                    }
                                })
                            })
                        })
                    }, 1000);
                }, function () {

                })
            }, 2000);
        })
    } else {
        if (index > 0) {
            index--;
            map(index).then();
        } else {
            updateDestArray().then();
        }
    }
}

login();
