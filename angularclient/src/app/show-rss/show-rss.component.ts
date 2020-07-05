import {Component, OnInit} from '@angular/core';
import {RssServiceService} from "../api/rss-service.service";
import {ListFlux} from "../model/list-flux";

@Component({
  selector: 'app-show-rss',
  templateUrl: './show-rss.component.html',
  styleUrls: ['./show-rss.component.scss']
})
export class ShowRssComponent implements OnInit {

  listFlux: ListFlux;

  constructor(private rssServiceService: RssServiceService) {
  }

  ngOnInit(): void {
    this.rssServiceService.getRss()
      .subscribe((list: ListFlux) => {
        console.log("liste", list);
        this.listFlux = list;
      });
  }

}
