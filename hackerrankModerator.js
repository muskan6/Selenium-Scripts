require('chromedriver');
const {Builder, Key, By, until} = require('selenium-webdriver');
var webdriver = require('selenium-webdriver');
var browser = new Builder().withCapabilities(webdriver.Capabilities.chrome()).build();
const TIMEOUT = 20000;
var url = "https://www.hackerrank.com/auth/login?h_l=body_middle_left_button&h_r=login";
//await browser.manage().setTimeouts( { implicit: TIMEOUT, pageLoad: TIMEOUT} );
async function hackMod() {
    try {
        browser.get(url);
        var username = "muskan99232@gmail.com";
        var password = "Muskan.15";
        //var username = "maliksachi4@gmail.com";
        //var password = "pepcoding@1234";
        browser.findElement(By.id("input-1")).sendKeys(username).then(() => {
            "Username entered successfully"
        });
        browser.findElement(By.id("input-2")).sendKeys(password).then(() => {
            "Password entered successfully"
        });
        //login button
        browser.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div[2]/div/div/div[2]/div/div/div[2]/div[1]/form/div[4]/button")).click().then(() => {
            "Login successful"
        });
        /*
        var urlAfterLogin = browser.getCurrentUrl().then(() => {
            if(urlAfterLogin == url){
                console.log("im here")
                browser.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                browser.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div[2]/div/div/div[2]/div/div/div[2]/div[2]/ul/li[2]/button")).click().then();
            }
        });
        */
        //Implicit waiting -> tells the webdriver to wait for an element to load by the time unit as provided.
        await browser.manage().setTimeouts({implicit: TIMEOUT, pageLoad: TIMEOUT});
        //profile button -> exception
        browser.findElement(By.xpath("/html/body/div[4]/div/div/div/div[1]/nav/div/div[2]/ul[2]/li[3]/div/a")).click().then(()=>{},()=>{});
        await browser.manage().setTimeouts({implicit: TIMEOUT, pageLoad: TIMEOUT});
        //admin button
        browser.findElement(By.xpath("/html/body/div[4]/div/div/div/div[1]/nav/div/div[2]/ul[2]/li[3]/div/div/ul/li[7]/a")).click().then(()=>{}, ()=>{});
        //manage challenges page
        browser.get("https://www.hackerrank.com/administration/challenges");

        var currenturl = browser.getCurrentUrl();
        var newurl = "";
        var links = [];
        var totalpages = 0;

        await browser.findElements(By.css("#content > div > div > div > section > div.pagination-wrap.clearfix.pagination-wrapper > div.pagination > ul > li")).then(
            async function (elements) {
                totalpages = elements.length - 3; // you get no. of pages + 4 elements, 1)<<  2)< 3)> 4)>>
            }
        );

        for (var x = 0; x < totalpages; x++) {
            try {
                await browser.manage().setTimeouts({implicit: TIMEOUT, pageLoad: TIMEOUT});
                //var millisecondsToWait = 3000;
                await browser.findElements(By.css(".backbone.block-center")).then(function (elements) {
                    elements.forEach(function (element) {
                        element.getAttribute("href").then(function (text) {
                            links.push(text);
                        });
                    });
                    browser.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                    browser.manage().setTimeouts({implicit: TIMEOUT}).then();
                    browser.findElement(By.css("#content > div > div > div > section > div.pagination-wrap.clearfix.pagination-wrapper > div.pagination > ul > li:nth-child(" + (totalpages + 2) + ") > a")).click().then(() => {
                        console.log("more pages exist");
                    }, () => {
                        console.log("no more pages");
                    });
                    currenturl = newurl;
                    newurl = browser.getCurrentUrl();
                });
            } catch (e) {
                //console.log(e);
            }
        }

        //call the recursive function
        autoSetModerator(links, links.length - 1);

        function autoSetModerator(links, counter) {
            browser.get(links[counter]).then(
                function () {
                    setTimeout(function () {
                        browser.findElement(By.xpath("/html/body/div[2]/div[10]/div/section/header/div/div[2]/ul/li[2]/a")).click().then(function () {
                            browser.findElement(By.xpath("/html/body/div[2]/div[10]/div/section/div/div/div[1]/div/div/input")).sendKeys("sumeet_malik1188").then(function () {
                                setTimeout(function () {
                                    browser.findElement(By.xpath("/html/body/div[2]/div[10]/div/section/div/div/div[1]/div/div/input")).sendKeys(Key.ENTER).then(function () {
                                        browser.findElement(By.xpath("/html/body/div[2]/div[10]/div/div/div/div/button[2]")).click().then(
                                            function () {
                                                if (counter > 0) {
                                                    counter--
                                                    autoSetModerator(links, counter);
                                                } else {
                                                    browser.quit().then();
                                                }
                                            }, function () {
                                                console.log("failed to save");
                                            }
                                        );
                                    }, function () {
                                        console.log("failed to press enter");
                                    });
                                }, 4000)
                            }, function () {
                                console.log("failed to add moderator ");
                            });
                        }, function () {
                            console.log("failed to open moderators");
                        });
                    }, 4000)
                }, function () {
                    console.log("unable to open link");
                }
            );
        }
    } catch (e) {
        console.log(e);
    }
}

hackMod().then(() => {
    console.log("Successful");
}, () => {
    console.log("Unsuccessful");
});
