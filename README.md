## Synopsis
Alexa Web Information Service SDK

Simple java sdk for AWIS 

https://aws.amazon.com/awis/


For now it lets you call and map only UrlInfo action.


## Code Example


     Alexa alexa = new Alexa("access_key_id", "secret");

        try {


            UrlInfoResponse response = alexa.newRequestBuilder()
                    .setAction(Actions.UrlInfo)
                    .setTargetUrl("www.corrieredellosport.it")
                    .addResponseGroup("Categories")
                    .addResponseGroup("Language")
                    .create()
                    .toObservable(UrlInfoResponse.class)
                    .toBlocking().first();


        }catch (Exception e){


            System.out.print("error");
        }



## License
The MIT License (MIT)
Further resources on The MIT License (MIT)
The MIT License (MIT)
Copyright (c) <year> <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.