import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ListFlux} from "../model/list-flux";
import {Observable} from "rxjs";
import {ListRss} from "../model/list-rss";

@Injectable({
  providedIn: 'root'
})
export class RssServiceService {

  private rssUrl = 'api/read';

  private listRssUrl = 'api/list-rss';

  constructor(private http: HttpClient) {
  }

  getRss(): Observable<ListFlux> {
    return this.http.get<ListFlux>(this.rssUrl);
  }

  getListRss(): Observable<ListRss> {
    return this.http.get<ListRss>(this.listRssUrl);
  }
}
