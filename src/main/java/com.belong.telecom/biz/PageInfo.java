package com.belong.telecom.biz;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PageInfo {

    protected Integer page;
    protected Integer size;
    protected long total;

}
