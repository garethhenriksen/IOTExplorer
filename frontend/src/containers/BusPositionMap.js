import React, { useEffect, useRef, useState } from "react";

import L from "leaflet";
import axios from "../axios-football";

const style = {
  height: "75vh"
};
const BusPositionMap = props => {
  const mapRef = useRef(null);
  const [markers, setMarkers] = useState(null);

  var markersLayer = new L.LayerGroup();
  useEffect(() => {
    mapRef.current = L.map("map", {
      center: [39.76, -104.98],
      zoom: 10,
      style: { height: "75vh" },

      layers: [
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
          attribution:
            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
          bounds: [
            [-85.0511287776, -179.999999975],
            [85.0511287776, 179.999999975]
          ],
          minZoom: 1,
          maxZoom: 20,
          format: "jpg",
          time: "",
          tilematrixset: "GoogleMapsCompatible_Level"
        })
      ]
    });
  }, []);

  const markersRef = useRef(null);
  let marker;
  useEffect(() => {
    const interval = setInterval(() => {
        axios
        .get("/bus")
        .then(res => {
            markersLayer.clearLayers();

          console.log(res.data);
          res.data.busPositionList.map(element => {
            const label = element.milesPerHour;
            
            marker  = L.marker([element.location.latitude, element.location.longitude])
              .bindPopup(label);
              markersLayer.addLayer(marker);
          })
        })
        .catch(err => {
          console.log(err.message);
        });
    }, 2000);
    return () => clearInterval(interval);
  }, [markers]);
  return <div id="map" style={style} />;
};

export default BusPositionMap;
