/**
 * Created by Administrator on 2018/1/25.
 */
load("nashorn:mozilla_compat.js");
importPackage(java.lang);
importPackage("cn.edu.lingnan.utils")

function  textFunction (event) {
    System.out.println(event);
}
function handleContextMenu(event, controller) {
    controller.handleContextMenu(event);
}