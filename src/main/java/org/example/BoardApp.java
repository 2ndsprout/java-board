package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class BoardApp {
    Scanner scan = new Scanner(System.in);
    int latestArticleId = 4; // 시작 번호를 1로 지정
    int WRONG_VALUE = 0;
    ArrayList<Article> articleList = new ArrayList<>();
    ArrayList<Customer> customerList = new ArrayList<>();
    Customer loggedInUser = null;


    public void run() {

        test();

        label:
        while (true) { // 반복 조건이 true 이기 때문에 무한 반복

            System.out.print("명령어");
            if (loggedInUser != null) {
                System.out.print("[" + loggedInUser.getId() + "(" + loggedInUser.getNickname() + ")]");
            }
            System.out.print(" : ");
            String cmd = scan.nextLine();

            switch (cmd) {
                case "exit":  // 숫자가 아닌 경우 같다라는 표현을 할 때 == 이 아닌 .equals()를 사용해야 한다.
                    System.out.println("프로그램을 종료합니다.");
                    break label; // 반복문 탈출

                case "add":

                    add();

                    break;
                case "list":

                    list();

                    break;
                case "update":

                    update();

                    break;
                case "delete":

                    delete();

                    break;
                case "detail":

                    detail();

                    break;
                case "search":

                    search();
                    break;
                case "signup":

                    signup();

                    break;
                case "login":

                    login();

                    break;
            }
        }
    }

    private void login() {

        System.out.print("아이디 : ");
        String id = scan.nextLine();
        System.out.print("비밀번호 : ");
        String password = scan.nextLine();

        for (Customer customer : customerList) {
            if (customer.getId().equals(id) && customer.getPassword().equals(password)) {
                loggedInUser = customer;
                System.out.println(loggedInUser.getNickname() + "님 환영합니다!");
                test();
                return;
            }
        }
        System.out.println("아이디나 비밀번호가 틀렸거나 잘못된 회원정보입니다.");


    }

    private void signup() {
        System.out.println("==== 회원 가입을 진행합니다 ====");
        System.out.print("아이디를 입력해주세요 : ");
        String id = scan.nextLine();
        System.out.print("비밀번호를 입력해주세요 : ");
        String password = scan.nextLine();
        System.out.print("닉네임을 입력해주세요 : ");
        String nickname = scan.nextLine();

        Customer customer = new Customer(id, password, nickname);
        customerList.add(customer);

        System.out.println("==== 회원가입이 완료되었습니다. ====");
    }

    public void printArticleList(ArrayList<Article> targetList) {
        for (Article article : targetList) {

            System.out.println("번호 : " + article.getId());
            System.out.printf("제목 : %s\n", article.getTitle());
            System.out.println("===================");
        }
    }

    private void search() {
        // 검색어를 입력
        System.out.println("검색 키워드를 입력해주세요 :");
        String keyword = scan.nextLine();

        ArrayList<Article> searchedList = new ArrayList<>();

        for (Article article : articleList) {
            if (article.getTitle().contains(keyword)) {
                searchedList.add(article);
            }
        }
        if (loggedInUser != null) {
            ArrayList<Article> userArticles = loggedInUser.getArticleList();
            for (Article article : userArticles) {
                if (article.getTitle().contains(keyword)) {
                    searchedList.add(article);
                }
                printArticleList(searchedList);
            }
        }
    }

    private void test() {

        Article a1 = new Article(1, "안녕하세요 반갑습니다. 자바 공부중이에요.", "인사", 0, 0, getCurrentDateTime());
        Article a2 = new Article(2, "자바 질문좀 할게요~", "질문", 0, 0, getCurrentDateTime());
        Article a3 = new Article(3, "정처기 따야되나요?", "문의", 0, 0, getCurrentDateTime());


        if (loggedInUser != null){
            ArrayList<Article> userArticles = loggedInUser.getArticleList();
            userArticles.add(a1);
            userArticles.add(a2);
            userArticles.add(a3);

        }

        else {
            articleList.add(a1);
            articleList.add(a2);
            articleList.add(a3);
        }
    }

    private void detail() {

        System.out.print("상세보기 할 게시물 번호를 입력해주세요 : ");
        int inputId = getParamAsInt(scan.nextLine(),WRONG_VALUE);

        if (inputId == WRONG_VALUE) {

            return;
        }
        int index = findIndexById(inputId);
        if (loggedInUser != null) {

            ArrayList<Article> userArticles = loggedInUser.getArticleList();

            Article article = userArticles.get(index);
            article.increaseView();
            ArrayList<Comments> commentsList = article.getComments();

            // alt + ctrl + L : 코드 정리. 자주 사용할 것
            System.out.println("====================");
            System.out.println("번호 : " + article.getId());
            System.out.println("제목 : " + article.getTitle());
            System.out.println("내용 : " + article.getBody());
            System.out.println("등록 날짜 : " + article.getRegDate());
            System.out.println("조회수 : " + article.getView());
            if (article.getHit() > 0) {
                System.out.println("추천수 : " + article.getHit());
            }
            System.out.println("====================");
            if (commentsList != null) {
                System.out.println("======= 댓글 =======");
                for (Comments comment : commentsList) {
                    System.out.println("댓글 내용 : " + comment.getComments());
                    System.out.println("댓글 등록 날짜 : " + comment.getCommentsRegDate());
                    System.out.println("====================");
                }
            }

            System.out.println("상세보기 기능을 선택해주세요.\n1. 댓글 등록\n2. 추천\n3. 수정\n4. 삭제\n5. 목록으로");
            int choice = getParamAsInt(scan.nextLine(),WRONG_VALUE);

            if (choice == WRONG_VALUE) {

                return;
            }

            switch (choice) {
                case 1:
                    System.out.print("댓글을 입력하세요 : ");
                    String comments = scan.nextLine();
                    String commentRegDay = getCurrentDateTime();

                    if (commentsList == null) {
                        commentsList = new ArrayList<>();
                        article.setComments(commentsList);
                    }
                    article.addComment(comments, commentRegDay);
                    System.out.println("댓글 등록이 완료되었습니다.");

                    break;

                case 2:
                    article.increaceHit();
                    System.out.println("추천 되었습니다.");

                    break;

                case 3:
                    System.out.print("수정하실 제목을 입력해주세요 : ");
                    String updateTitle = scan.nextLine();
                    article.setTitle(updateTitle);
                    System.out.print("수정하실 내용을 입력해주세요 : ");
                    String updateBody = scan.nextLine();
                    article.setBody(updateBody);
                    System.out.println(loggedInUser.getNickname() + "님의 " + inputId + "번 게시물 수정이 완료되었습니다.");

                    break;

                case 4:
                    System.out.print("삭제 하시겠습니까? (y/n) : ");
                    String answer = scan.nextLine();
                    if (answer.equals("y")) {
                        userArticles.remove(article);
                        System.out.println(loggedInUser.getNickname() + "님의 " + inputId + "번 게시물을 삭제했습니다.");
                    }
                    else if (answer.equals("n")) {
                        System.out.println("삭제를 취소합니다.");
                    }
                    break;

                case 5:
                    System.out.println("목록으로 돌아갑니다.");
            }
        }
        else {
            Article article = articleList.get(index);
            article.increaseView();
            ArrayList<Comments> commentsList = article.getComments();

            // alt + ctrl + L : 코드 정리. 자주 사용할 것
            System.out.println("====================");
            System.out.println("번호 : " + article.getId());
            System.out.println("제목 : " + article.getTitle());
            System.out.println("내용 : " + article.getBody());
            System.out.println("등록 날짜 : " + article.getRegDate());
            System.out.println("조회수 : " + article.getView());
            if (article.getHit() > 0) {
                System.out.println("추천수 : " + article.getHit());
            }
            System.out.println("====================");
            if (commentsList != null ) {
                System.out.println("======= 댓글 =======");
                for (Comments comment : commentsList) {
                    System.out.println("댓글 내용 : " + comment.getComments());
                    System.out.println("댓글 등록 날짜 : " + comment.getCommentsRegDate());
                    System.out.println("====================");
                }
            }


            System.out.println("상세보기 기능을 선택해주세요.\n1. 댓글 등록\n2. 추천\n3. 수정\n4. 삭제\n5. 목록으로");
            int choice = getParamAsInt(scan.nextLine(),WRONG_VALUE);

            if (choice == WRONG_VALUE) {

                return;
            }

            switch (choice) {
                case 1:
                    System.out.print("댓글을 입력하세요 : ");
                    String comments = scan.nextLine();
                    String commentRegDay = getCurrentDateTime();

                    if (commentsList == null) {
                        commentsList = new ArrayList<>();
                        article.setComments(commentsList);
                    }
                    article.addComment(comments, commentRegDay);
                    System.out.println("댓글 등록이 완료되었습니다.");

                    break;

                case 2:
                    article.increaceHit();
                    System.out.println("추천 되었습니다.");

                    break;

                case 3:
                    System.out.print("수정하실 제목을 입력해주세요 : ");
                    String updateTitle = scan.nextLine();
                    article.setTitle(updateTitle);
                    System.out.print("수정하실 내용을 입력해주세요 : ");
                    String updateBody = scan.nextLine();
                    article.setBody(updateBody);
                    System.out.println(inputId + "번 게시물 수정이 완료되었습니다.");

                    break;

                case 4:
                    System.out.print("삭제 하시겠습니까? (y/n) : ");
                    String answer = scan.nextLine();
                    if (answer.equals("y")) {
                        articleList.remove(article);
                        System.out.println(inputId + "번 게시물을 삭제했습니다.");
                    }
                    else if (answer.equals("n")) {
                        System.out.println("삭제를 취소합니다.");
                    }

                    break;

                case 5:
                    System.out.println("목록으로 돌아갑니다.");
            }
        }


    }

    private void delete() {
        System.out.print("삭제할 게시물 번호를 입력해주세요 : ");
        int inputId = getParamAsInt(scan.nextLine(),WRONG_VALUE);

        if (inputId == WRONG_VALUE) {

            return;
        }
        if (inputId == -1) {
            System.out.println("없는 게시물입니다.");
            return;
        }
        if (loggedInUser != null){
            ArrayList<Article> userArticles = loggedInUser.getArticleList();
            userArticles.remove(inputId);
        }
        else {
            articleList.remove(inputId);
        }

        System.out.printf("%d번 게시물이 삭제되었습니다.\n", inputId);

    }

    private void add() {
        System.out.print("게시물 제목을 입력해주세요 : ");
        String title = scan.nextLine();

        System.out.print("게시물 내용을 입력해주세요 : ");
        String body = scan.nextLine();
        String regDate = getCurrentDateTime();

        // 모든 매개변수를 받는 생성자 이용


        Article article = new Article(latestArticleId, title, body, 0, 0, regDate);
        if (loggedInUser != null) {

            loggedInUser.addArtcleList(article);
        }
        else {

            articleList.add(article);
        }
        System.out.println("게시물이 등록되었습니다.");
        latestArticleId++; // 게시물이 생성될 때마다 번호를 증가
    }

    public void list() {
        System.out.println("====================");
        if (loggedInUser != null) {

            ArrayList<Article> userArticles = loggedInUser.getArticleList();

            if (userArticles.isEmpty()) {

                System.out.println("작성한 게시물이 없습니다.");

            } else {
                for (Article article : userArticles) {
                    System.out.println("번호 : " + article.getId());
                    System.out.println("제목 : " + article.getTitle());
                    System.out.println("등록 날짜 : " + article.getRegDate());
                    System.out.println("====================");

                }
            }

        }
        else {

            for (Article article : articleList) {

                System.out.println("번호 : " + article.getId());
                System.out.println("제목 : " + article.getTitle());
                System.out.println("등록 날짜 : " + article.getRegDate());
                System.out.println("====================");
            }
        }
    }

    public void update() {
        System.out.print("수정할 게시물 번호를 입력해주세요 : ");
        int inputId = getParamAsInt(scan.nextLine(),WRONG_VALUE);

        if (inputId == WRONG_VALUE) {

            return;
        }

        int index = findIndexById(inputId);
        if (index == -1) {
            System.out.println("없는 게시물입니다.");
            return;
        }
        System.out.print("새로운 제목을 입력해주세요 : ");
        String newTitle = scan.nextLine();

        System.out.print("새로운 내용을 입력해주세요 : ");
        String newBody = scan.nextLine();

        if (loggedInUser != null){

        ArrayList<Article> userArticles = loggedInUser.getArticleList();
            Article target = userArticles.get(index);
            target.setTitle(newTitle); // target 은 참조값이므로 직접 객체를 접근하여 수정 가능
            target.setBody(newBody);
        }
        else {
            Article target = articleList.get(index);
            target.setTitle(newTitle); // target 은 참조값이므로 직접 객체를 접근하여 수정 가능
            target.setBody(newBody);
        }
        System.out.printf("%d번 게시물이 수정되었습니다.\n", inputId);

    }

    // 입력 : 찾고자 하는 게시물 번호
    // 출력 : 게시물 번호에 해당하는 인덱스
    public int findIndexById(int id) {

        if (loggedInUser != null) {
            ArrayList<Article> userArticles = loggedInUser.getArticleList();
            for (int i = 0; i < userArticles.size(); i++) {
                Article article = userArticles.get(i);

                if (article.getId() == id) {
                    return i; // 원하는 것은 찾은 즉시 종료.
                }
            }
        } else {
            for (int i = 0; i < articleList.size(); i++) {
                Article article = articleList.get(i);

                if (article.getId() == id) {
                    return i;
                }
            }
        }
        return -1;
    }
    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();

        // 날짜와 시간의 형식을 지정합니다. 여기서는 연-월-일 시:분:초 형식을 사용합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        // 지정한 형식으로 날짜와 시간을 출력합니다.
        return now.format(formatter);
    }
    private int getParamAsInt(String param, int defaultValue) {
        try {
            return Integer.parseInt(param);

        } catch (NumberFormatException e) {

            System.out.println("숫자를 입력해주세요.");
            return defaultValue;
        }
    }
}
