import json, re

import requests
from flask import Flask, Response, request
from bs4 import BeautifulSoup

# Flask setting
app = Flask(__name__)

# Requests setting
BASE_URL = "http://aulib.asia.edu.tw"

NEW_BOOK_URL = BASE_URL + "/webpac/js/newbook_json_data.json"
THEME_BOOK_URL = BASE_URL + "/webpac/js/themebook_json_data.json"
SEARCH_BOOK_URL = BASE_URL + "/webpac/search.cfm"
BOOKS_URL = BASE_URL + "/webpac/content.cfm?mid="

@app.route("/search/<book_title>")
def search_book(book_title):
        
    params = {
        'm': 'ss',
        'k0': book_title,
        't0': 'k',
        'c0': 'and',
        'current_page': request.args.get('current_page'),
        'list_num': '25',
    }
    res = requests.get(SEARCH_BOOK_URL, params=params)
    soup = BeautifulSoup(res.text, "lxml")

    book_list = soup.find_all(class_="list_box")
    return_json = []

    for item in book_list:

        sub_item_1 = item.select(".title ul li a")[0]
        sub_item_2 = item.select(".product_img img")[0]
        sub_item_3 = item.select(".product_info_content p")[1]

        return_json.append({
            'name': sub_item_1['title'],
            'book_id': sub_item_1['href'].split("?")[1].split("&")[0].split("=")[1],
            'photo_url': sub_item_2['src'] if sub_item_2['src'].startswith("http") else BASE_URL + "/webpac/" + sub_item_2['src'],
            'author': re.split("[:：]", sub_item_3.text)[1].strip(),
        })

    return Response(json.dumps(return_json, ensure_ascii=False), mimetype='application/json')

@app.route("/new_book")
def new_book():
    res = requests.get(NEW_BOOK_URL)
    j = json.loads(res.text)

    for book in j['book']:
        book['book_id'] = book.pop('detail_info').split("?")[1].split("&")[0].split("=")[1]
        book['photo_url'] = book.pop('url')
        if (not book['photo_url'].startswith("http")):
            book['photo_url'] = BASE_URL + "/webpac/" + book['photo_url']
        # print(book)

    return Response(json.dumps(j['book'], ensure_ascii=False), mimetype='application/json')

@app.route("/theme_book")
def theme_book():
    res = requests.get(THEME_BOOK_URL)
    j = json.loads(res.text)

    for book in j['book']:
        book['book_id'] = book.pop('detail_info').split("?")[1].split("&")[0].split("=")[1]
        book['photo_url'] = book.pop('url')
        if (not book['photo_url'].startswith("http")):
            book['photo_url'] = BASE_URL + "/webpac/" + book['photo_url']
        # print(book)

    return Response(json.dumps(j['book'], ensure_ascii=False), mimetype='application/json')

@app.route("/books/<int:book_id>")
def book_detail_info(book_id):
    res = requests.get(BOOKS_URL + str(book_id))
    soup = BeautifulSoup(res.text, "lxml")

    return_json = {}

    # title, author, publisher, ISBN, photo
    info = soup.find(class_="info")
    apy = info.select("p")[0].text.strip().replace("\t", "").split("\n")

    book_title = info.find("h2").text
    book_author = re.split("[:：]", apy[0], 1)[1]
    book_publishers = re.split("[:：]", apy[1], 1)[1]
    book_publication_year = re.split("[:：]", apy[2], 1)[1]
    book_isbn = re.split("[:：]", info.select("p")[1].text, 1)[1]
    book_photo = soup.find(class_='photo').find("img")['src']
    if not book_photo.startswith("http"):
        book_photo = BASE_URL + "/webpac/" + book_photo

    return_json['title'] = book_title
    return_json['author'] = book_author
    return_json['publishers'] = book_publishers
    return_json['publication_year'] = book_publication_year
    return_json['isbn'] = book_isbn
    return_json['photo'] = book_photo

    # collections in library
    collections_json = []
    try:
        collections = soup.find(class_="list list_border").find_all("tbody")
        for tbody in collections:
            tr = tbody.find_all("td")
            collections_json.append({
                "locate": tr[1].text.strip(),
                "call_number": tr[2].text.strip(),
                "status": re.sub("[\n\t ]+", "-", tr[3].text.strip()),
                "item_class": tr[9].text.strip(),
            })
        pass
    except AttributeError as AttrError:
        print(AttrError)

    return_json['collections'] = collections_json

    return Response(json.dumps(return_json, ensure_ascii=False), mimetype='application/json')

if __name__ == "__main__":
    app.run(debug=True)
    