



## 操作元素



### 改变元素内容

```javascript
element.innerText
```

- **非标准**
- **不识别HTML标签**，将标签当做字符串处理
- 获取元素时，去除HTML标签，同时也去除空格和换行



```javascript
element.innerHTML
```

- **W3C标准，使用较多**
- **识别HTML标签**
- 获取元素时，保留HTML标签，同时也保留空格和换行、



这两个属性是可读写的，可以用这两个属性来获取元素里的内容。 

```javascript
var p = document.querySelector('p');
console.log(p.innerText);
```





```javascript
btn.onclick = function() {
    input.value = '被点击了';
    // this指向的是事件函数的的调用者，这里指向btn
    this.disabled = true;
}
```



